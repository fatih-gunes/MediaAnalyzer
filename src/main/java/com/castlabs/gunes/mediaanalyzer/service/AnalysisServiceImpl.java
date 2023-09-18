package com.castlabs.gunes.mediaanalyzer.service;

import com.castlabs.gunes.mediaanalyzer.web.model.Box;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    @Override
    public List<Box> getMediaData(String mediaFileUrl) throws IOException {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(mediaFileUrl, byte[].class);
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

            Set<String> locations = new HashSet<>();
            while (statusCode == HttpStatus.MOVED_PERMANENTLY) {
                responseEntity = getNextResponseEntity(restTemplate, responseEntity, locations);
                statusCode = (HttpStatus) responseEntity.getStatusCode();
            }

            List<Box> boxes = new ArrayList<>();
            if (statusCode == HttpStatus.OK) {
                byte[] responseData = responseEntity.getBody();

                ByteBuffer buffer = ByteBuffer.allocate(4);
                try (InputStream inputStream = new ByteArrayInputStream(Objects.requireNonNull(responseData))) {
                    // Read and analyze the media container structure
                    while (inputStream.read(buffer.array()) != -1) {
                        Box box = readBox(inputStream, buffer);
                        boxes.add(box);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                throw new ResponseStatusException(responseEntity.getStatusCode());
            }
            return boxes;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static ResponseEntity<byte[]> getNextResponseEntity(RestTemplate restTemplate, ResponseEntity<byte[]> responseEntity, Set<String> locations) throws IOException {
        HttpHeaders headers = responseEntity.getHeaders();
        String newLocation = headers.getFirst(HttpHeaders.LOCATION);

        if (locations.contains(newLocation) || newLocation == null) {
            throw new IOException("File location not found or infinite redirect loop detected.");
        }
        locations.add(newLocation);

        responseEntity = restTemplate.getForEntity(newLocation, byte[].class);
        return responseEntity;
    }

    private Box readBox(InputStream inputStream, ByteBuffer buffer) throws IOException {
        int boxSize = buffer.getInt(0);
        String boxType = readBoxType(inputStream);

        Box box = new Box(boxType, boxSize);

        if ("MOOF".equalsIgnoreCase(boxType) || "TRAF".equalsIgnoreCase(boxType)) {
            // Read and process sub-boxes recursively
            while (boxSize > 8 && inputStream.read(buffer.array()) != -1) {
                Box subBox = readBox(inputStream, buffer);
                box.addSubBox(subBox);
                boxSize -= subBox.getBoxSize();
            }
        } else {
            readAndSetBoxData(inputStream, boxSize, box);
        }

        return box;
    }

    private static void readAndSetBoxData(InputStream inputStream, int boxSize, Box box) throws IOException {
        byte[] boxData = new byte[boxSize - 8]; // Calculate the payload data size
        inputStream.read(boxData);
        box.setData(boxData);
    }

    private String readBoxType(InputStream inputStream) throws IOException {
        byte[] typeBytes = new byte[4];
        inputStream.read(typeBytes);
        return new String(typeBytes);
    }

}
