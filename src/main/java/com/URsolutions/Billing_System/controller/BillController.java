package com.URsolutions.Billing_System.controller;

import com.URsolutions.Billing_System.dto.BillDTO;
import com.URsolutions.Billing_System.dto.BillItemDTO;
import com.URsolutions.Billing_System.model.Bill;
import com.URsolutions.Billing_System.model.BillItem;
import com.URsolutions.Billing_System.repository.BillRepository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    // ✅ Create bill via DTO
    @PostMapping
    public Bill createBill(@RequestBody BillDTO billDTO) {
        Bill bill = new Bill();
        bill.setCustomerName(billDTO.getCustomerName());
        bill.setCustomerContact(billDTO.getCustomerContact());
        bill.setTotalAmount(billDTO.getTotalAmount());
        bill.setDiscount(billDTO.getDiscount());
        bill.setPayableAmount(billDTO.getPayableAmount());

        List<BillItem> items = billDTO.getItems().stream().map(dto -> {
            BillItem item = new BillItem();
            item.setProductName(dto.getProductName());
            item.setPrice(dto.getPrice());
            item.setQuantity(dto.getQuantity());
            item.setTotal(dto.getTotal());
            item.setBill(bill); // 🔗 link to bill
            return item;
        }).collect(Collectors.toList());

        bill.setItems(items);
        return billRepository.save(bill);
    }

    // ✅ Get all bills
    @GetMapping
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    // ✅ Get specific bill by ID
    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billRepository.findById(id).orElseThrow();
    }

    // ✅ Generate PDF for a specific bill
    @GetMapping("/{id}/pdf")
    public void generatePdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Bill bill = billRepository.findById(id).orElseThrow();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Optional logo (only if logo.png exists at that path)
        try {
            Image logo = Image.getInstance("src/main/resources/static/logo.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_LEFT);
            document.add(logo);
        } catch (Exception e) {
            // Skip if image not found
        }

        // Title
        Paragraph title = new Paragraph("🧾 BILL INVOICE");
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Company info
        Font font = FontFactory.getFont(FontFactory.TIMES_ITALIC, 11, BaseColor.BLACK);

        Paragraph company = new Paragraph("💼 SANVI LUBRITECH SOLUTION \n");
        company.getFont().setStyle("bold");
        company.setAlignment(Element.ALIGN_CENTER);
        company.getFont().setStyle("underlined");
        company.getFont().setSize(20);
        document.add(company);

        Paragraph address = new Paragraph("\n📍 Loni BK., Tal. Rahata, Dist. Ahilyanagar,\nMaharashtra - 413736", font);
        Paragraph contact1 = new Paragraph("📞 Deepak Vikhe - +91 7798609099", font);
        Paragraph contact2 = new Paragraph("📞 Amol Mapari - +91 9270557017", font);
        Paragraph email = new Paragraph("✉ vikhe.deepak@gmail.com", font);

        address.setAlignment(Element.ALIGN_RIGHT);
        contact1.setAlignment(Element.ALIGN_RIGHT);
        contact2.setAlignment(Element.ALIGN_RIGHT);
        email.setAlignment(Element.ALIGN_RIGHT);

        document.add(address);
        document.add(contact1);
        document.add(contact2);
        document.add(email);
        document.add(new Paragraph(" "));

        // Bill info
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.addCell("👤 Customer Name: " + bill.getCustomerName() + "\n\n📞 Contact No.: " + bill.getCustomerContact());
        infoTable.addCell("🧾 Bill ID: " + bill.getId() + "\n\n📅 Date: " + bill.getCreatedAt());
        document.add(infoTable);
        document.add(new Paragraph(" "));

        // Items table
        PdfPTable productTable = new PdfPTable(4);
        productTable.setWidthPercentage(100);
        productTable.addCell("Product");
        productTable.addCell("Price");
        productTable.addCell("Quantity");
        productTable.addCell("Total");

        for (BillItem item : bill.getItems()) {
            productTable.addCell(item.getProductName());
            productTable.addCell("₹" + item.getPrice());
            productTable.addCell(String.valueOf(item.getQuantity()));
            productTable.addCell("₹" + item.getTotal());
        }

        document.add(productTable);
        document.add(new Paragraph(" "));

        // Summary
        document.add(new Paragraph("Total: ₹" + bill.getTotalAmount()));
        document.add(new Paragraph("Discount: ₹" + bill.getDiscount()));
        Paragraph payable = new Paragraph("Payable Amount: ₹" + bill.getPayableAmount());
        payable.getFont().setStyle("bold");
        document.add(payable);
        document.add(new Paragraph(" "));

        // Signature
        Paragraph signature = new Paragraph("\n\n\nAdmin Signature:");
        signature.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(signature);

        document.close();
    }

    // ✅ Delete all bills
    @DeleteMapping("/deleteAll")
    public void deleteAllBills() {
        billRepository.deleteAll();
    }
}
