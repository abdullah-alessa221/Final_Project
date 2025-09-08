package com.example.finalprojectjavabootcamp.Service;

import com.adobe.pdfservices.operation.*;
import com.adobe.pdfservices.operation.auth.*;
import com.adobe.pdfservices.operation.io.*;
import com.adobe.pdfservices.operation.pdfjobs.jobs.DocumentMergeJob;
import com.adobe.pdfservices.operation.pdfjobs.params.documentmerge.*;
import com.adobe.pdfservices.operation.pdfjobs.result.DocumentMergeResult;
import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOOUT.CarInvoiceDTOOut;
import com.example.finalprojectjavabootcamp.Model.Invoice;
import com.example.finalprojectjavabootcamp.Repository.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    private final InvoiceRepository invoiceRepository;

    private PDFServices pdfServices;

    @Value("${PDF_SERVICES_CLIENT_ID}")
    private String PDF_ID;

    @Value("${PDF_SERVICES_CLIENT_SECRET}")
    private String PDF_SECRET;

    @PostConstruct
    public void initialize() {
        Credentials credentials = new ServicePrincipalCredentials(PDF_ID, PDF_SECRET);
        pdfServices = new PDFServices(credentials);
    }

    public Invoice createInvoice(CarInvoiceDTOOut invoiceDTO) throws ApiException {
        InputStream inputStream;
        Asset asset;
        JSONObject jsonDataForMerge;

        try {
            inputStream = new File("src/main/resources/Templates/invoice.docx")
                    .toURI().toURL().openStream();
            asset = pdfServices.upload(inputStream, PDFServicesMediaType.DOCX.getMediaType());
            jsonDataForMerge = new JSONObject(invoiceDTO);
        } catch (Exception e) {
            throw new ApiException("Could not create invoice file: " + e.getMessage());
        }

        DocumentMergeParams documentMergeParams = DocumentMergeParams.documentMergeParamsBuilder()
                .withJsonDataForMerge(jsonDataForMerge)
                .withOutputFormat(OutputFormat.PDF)
                .build();

        DocumentMergeJob documentMergeJob = new DocumentMergeJob(asset, documentMergeParams);

        try {
            String location = pdfServices.submit(documentMergeJob);
            PDFServicesResponse<DocumentMergeResult> pdfServicesResponse =
                    pdfServices.getJobResult(location, DocumentMergeResult.class);

            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            byte[] fileContent = IOUtils.toByteArray(streamAsset.getInputStream());


            String folderPath = "generated-pdfs/";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String fileName = "invoice_" + invoiceDTO.getId() + "_" + UUID.randomUUID() + ".pdf";
            File outputFile = new File(folderPath + fileName);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(fileContent);
            }

            Invoice invoice = new Invoice();
            invoice.setBuyerName(invoiceDTO.getBuyerName());
            invoice.setBuyerPhone(invoiceDTO.getBuyerPhone());
            invoice.setSellerName(invoiceDTO.getSellerName());
            invoice.setSellerPhone(invoiceDTO.getSellerPhone());
            invoice.setPrice(invoiceDTO.getPrice());
            invoice.setDate(invoiceDTO.getDate());
            invoice.setFilePath(outputFile.getAbsolutePath());

            return invoiceRepository.save(invoice);

        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public byte[] downloadInvoice(Integer id) throws ApiException {
        try {
            Invoice invoice = invoiceRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Invoice not found with id: " + id));

            File file = new File(invoice.getFilePath());
            if (!file.exists()) throw new ApiException("File not found: " + invoice.getFilePath());

            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            throw new ApiException("Could not download invoice: " + e.getMessage());
        }
    }


    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Integer id) throws ApiException {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Invoice not found with id: " + id));
    }

}
