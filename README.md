# AIS Encoder and Decoder Library

This Java library provides encoding and decoding for AIS (Automatic Identification System) messages, supporting AIS message types 1 (Position Reports) and 5 (Static and Voyage Data). It includes multipart message assembly, checksum verification, and conforms to ITU-R M.1371 specifications.

## Features

- Encode AIS Position (type 1) and Static Voyage (type 5) messages into valid NMEA AIVDM sentences.
- Decode single and multipart AIS NMEA sentences back to Java POJOs.
- Handles multipart message reassembly with timeout.
- Supports checksum verification.
- Includes extensive unit and integration tests.
- Designed for easy integration in Java projects.

## Usage

### Encoding

Use the `AisEncoder` class to encode AIS message objects into NMEA sentences:

```java
AisPositionMessage position = new AisPositionMessage();
// set fields...

List<String> sentences = AisEncoder.encodePositionMessage(position);
sentences.forEach(System.out::println);
```

### Decoding

Use the `AisDecoder` class to decode incoming NMEA sentences:

```java
AisDecoder decoder = new AisDecoder();
decoder.setPositionCallback(position -> System.out.println("Position: " + position));
decoder.setStaticCallback(staticMsg -> System.out.println("Static: " + staticMsg));

decoder.onSentence(nmeaSentence);
```

## Building with Maven

Add this project as a dependency once published, or build locally with:

```bash
mvn clean install
```

## Testing

Run unit and integration tests with:

```bash
mvn test
```

## License

MIT License — see LICENSE file.

---

For detailed API and examples, see [project documentation].
