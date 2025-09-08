package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOOUT.CarInvoiceDTOOut;
import com.example.finalprojectjavabootcamp.Model.Invoice;
import com.example.finalprojectjavabootcamp.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final FileService fileService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateInvoice(@RequestBody CarInvoiceDTOOut dto) {
        try {
            Invoice invoice = fileService.createInvoice(dto);
            return ResponseEntity.ok(new ApiResponse("Invoice generated successfully with ID: " + invoice.getId()));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadInvoice(@PathVariable Integer id) {
        try {
            byte[] file = fileService.downloadInvoice(id);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllInvoices() {
        return ResponseEntity.ok(fileService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(fileService.getInvoiceById(id));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
