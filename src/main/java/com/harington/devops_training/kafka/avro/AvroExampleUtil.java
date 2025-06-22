package com.harington.devops_training.kafka.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroExampleUtil {
    public static final String CONTRACT_SCHEMA_JSON = """
            {
              "type": "record",
              "name": "ContractAvro",
              "fields": [
                {"name": "id", "type": "string"}, 
                {"name": "amount", "type": "double"},
                {"name": "isEligible", "type": "boolean"}
              ]
            }
            """;

    public static Schema getContractSchema() {
        return new Schema.Parser().parse(CONTRACT_SCHEMA_JSON);
    }

    public static byte[] serializeContract(String id, double amount, boolean isEligible) throws IOException {
        Schema schema = getContractSchema();
        GenericRecord contract = new GenericData.Record(schema);
        contract.put("id", id);
        contract.put("amount", amount);
        contract.put("isEligible", isEligible);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(contract, encoder);
        encoder.flush();
        return out.toByteArray();
    }

    public static GenericRecord deserializeContract(byte[] data) throws IOException {
        Schema schema = getContractSchema();
        DatumReader<GenericRecord> reader = new SpecificDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        return reader.read(null, decoder);
    }

    public static void main(String[] args) throws Exception {
        // Exemple d'utilisation pédagogique d'Avro
        String id = "contrat-123";
        double amount = 15000.0;
        boolean isEligible = true;

        // Sérialisation en Avro (binaire)
        byte[] avroBytes = serializeContract(id, amount, isEligible);
        System.out.println("Taille du binaire Avro : " + avroBytes.length + " octets");

        // Désérialisation depuis Avro
        GenericRecord contract = deserializeContract(avroBytes);
        System.out.println("Contrat désérialisé depuis Avro :");
        System.out.println("id = " + contract.get("id"));
        System.out.println("amount = " + contract.get("amount"));
        System.out.println("isEligible = " + contract.get("isEligible"));
    }
}
