package com.rajmart;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // =========================================
    // GET ALL PRODUCTS
    // =========================================

    @GetMapping
    public List<Product> getProducts() {
        return ProductDAO.getAllProducts();
    }


    // =========================================
    // ADD PRODUCT + IMAGE
    // =========================================

    @PostMapping(
            value = "/add",
            consumes = "multipart/form-data"
    )
    public Map<String, Object> addProduct(

            @RequestParam("name")
            String name,

            @RequestParam("price")
            double price,

            @RequestParam("quantity")
            int quantity,

            @RequestParam(
                    value = "image",
                    required = false
            )
            MultipartFile image
    ) {

        Map<String, Object> response =
                new HashMap<>();


        // -------------------------
        // VALIDATION
        // -------------------------

        if (name == null ||
                name.trim().isEmpty()) {

            response.put("success", false);
            response.put(
                    "message",
                    "Product name is required."
            );

            return response;
        }


        if (price <= 0) {

            response.put("success", false);
            response.put(
                    "message",
                    "Price must be greater than zero."
            );

            return response;
        }


        if (quantity < 0) {

            response.put("success", false);
            response.put(
                    "message",
                    "Quantity cannot be negative."
            );

            return response;
        }


        // -------------------------
        // IMAGE PATH
        // -------------------------

        String imagePath = "";


        // -------------------------
        // SAVE IMAGE
        // -------------------------

        if (image != null &&
                !image.isEmpty()) {

            try {

                String originalName =
                        image.getOriginalFilename();


                String extension = "";


                if (originalName != null &&
                        originalName.contains(".")) {

                    extension =
                            originalName.substring(
                                    originalName.lastIndexOf(".")
                            ).toLowerCase();
                }


                // Allow only image types

                if (!extension.equals(".jpg") &&
                        !extension.equals(".jpeg") &&
                        !extension.equals(".png") &&
                        !extension.equals(".gif") &&
                        !extension.equals(".webp")) {

                    response.put("success", false);
                    response.put(
                            "message",
                            "Only JPG, JPEG, PNG, GIF and WEBP images are allowed."
                    );

                    return response;
                }


                // Unique file name

                String fileName =
                        UUID.randomUUID()
                                .toString()
                                + extension;


                // =========================================
                // SOURCE STATIC FOLDER
                // =========================================

                String sourceDirectory =
                        System.getProperty("user.dir")
                                + File.separator
                                + "src"
                                + File.separator
                                + "main"
                                + File.separator
                                + "resources"
                                + File.separator
                                + "static"
                                + File.separator
                                + "uploads"
                                + File.separator
                                + "products";


                File sourceFolder =
                        new File(sourceDirectory);


                if (!sourceFolder.exists()) {
                    sourceFolder.mkdirs();
                }


                File sourceFile =
                        new File(
                                sourceFolder,
                                fileName
                        );


                // Save first copy

                image.transferTo(sourceFile);


                // =========================================
                // TARGET STATIC FOLDER
                // =========================================

                String targetDirectory =
                        System.getProperty("user.dir")
                                + File.separator
                                + "target"
                                + File.separator
                                + "classes"
                                + File.separator
                                + "static"
                                + File.separator
                                + "uploads"
                                + File.separator
                                + "products";


                File targetFolder =
                        new File(targetDirectory);


                if (!targetFolder.exists()) {
                    targetFolder.mkdirs();
                }


                File targetFile =
                        new File(
                                targetFolder,
                                fileName
                        );


                // Copy image to target/classes

                java.nio.file.Files.copy(
                        sourceFile.toPath(),
                        targetFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );


                // Browser URL

                imagePath =
                        "/uploads/products/"
                                + fileName;


                System.out.println(
                        "IMAGE SAVED: " +
                                imagePath
                );


                System.out.println(
                        "SOURCE IMAGE: " +
                                sourceFile.getAbsolutePath()
                );


                System.out.println(
                        "TARGET IMAGE: " +
                                targetFile.getAbsolutePath()
                );


            } catch (Exception e) {

                System.out.println(
                        "IMAGE UPLOAD ERROR"
                );

                e.printStackTrace();


                response.put("success", false);
                response.put(
                        "message",
                        "Image upload failed."
                );

                return response;
            }
        }


        // -------------------------
        // SAVE PRODUCT TO DATABASE
        // -------------------------

        boolean success =
                ProductDAO.addProduct(
                        name.trim(),
                        price,
                        quantity,
                        imagePath
                );


        response.put(
                "success",
                success
        );


        if (success) {

            response.put(
                    "message",
                    "Product added successfully."
            );

            response.put(
                    "image",
                    imagePath
            );

        } else {

            response.put(
                    "message",
                    "Failed to add product."
            );
        }


        return response;
    }


    // =========================================
    // DELETE PRODUCT
    // =========================================

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(
            @PathVariable int id) {

        Map<String, Object> response =
                new HashMap<>();


        if (id <= 0) {

            response.put("success", false);

            response.put(
                    "message",
                    "Invalid product ID."
            );

            return response;
        }


        boolean success =
                ProductDAO.deleteProduct(id);


        response.put(
                "success",
                success
        );


        if (success) {

            response.put(
                    "message",
                    "Product deleted successfully."
            );

        } else {

            response.put(
                    "message",
                    "Product not found."
            );
        }


        return response;
    }


    // =========================================
    // UPDATE PRODUCT + IMAGE
    // =========================================

    @PutMapping(
            value = "/{id}",
            consumes = "multipart/form-data"
    )
    public Map<String, Object> updateProduct(

            @PathVariable int id,

            @RequestParam("name")
            String name,

            @RequestParam("price")
            double price,

            @RequestParam("quantity")
            int quantity,

            @RequestParam(
                    value = "image",
                    required = false
            )
            MultipartFile image
    ) {

        Map<String, Object> response =
                new HashMap<>();


        // -------------------------
        // VALIDATION
        // -------------------------

        if (id <= 0) {

            response.put("success", false);
            response.put(
                    "message",
                    "Invalid product ID."
            );

            return response;
        }


        if (name == null ||
                name.trim().isEmpty()) {

            response.put("success", false);
            response.put(
                    "message",
                    "Product name is required."
            );

            return response;
        }


        if (price <= 0) {

            response.put("success", false);
            response.put(
                    "message",
                    "Price must be greater than zero."
            );

            return response;
        }


        if (quantity < 0) {

            response.put("success", false);
            response.put(
                    "message",
                    "Quantity cannot be negative."
            );

            return response;
        }


        // -------------------------
        // KEEP OLD IMAGE
        // -------------------------

        String imagePath = "";


        List<Product> products =
                ProductDAO.getAllProducts();


        for (Product product : products) {

            if (product.getId() == id) {

                imagePath =
                        product.getImage();

                break;
            }
        }


        // -------------------------
        // UPLOAD NEW IMAGE
        // -------------------------

        if (image != null &&
                !image.isEmpty()) {

            try {

                String originalName =
                        image.getOriginalFilename();


                String extension = "";


                if (originalName != null &&
                        originalName.contains(".")) {

                    extension =
                            originalName.substring(
                                    originalName.lastIndexOf(".")
                            ).toLowerCase();
                }


                if (!extension.equals(".jpg") &&
                        !extension.equals(".jpeg") &&
                        !extension.equals(".png") &&
                        !extension.equals(".gif") &&
                        !extension.equals(".webp")) {

                    response.put("success", false);
                    response.put(
                            "message",
                            "Only JPG, JPEG, PNG, GIF and WEBP images are allowed."
                    );

                    return response;
                }


                String fileName =
                        UUID.randomUUID()
                                .toString()
                                + extension;


                // Source directory

                String sourceDirectory =
                        System.getProperty("user.dir")
                                + File.separator
                                + "src"
                                + File.separator
                                + "main"
                                + File.separator
                                + "resources"
                                + File.separator
                                + "static"
                                + File.separator
                                + "uploads"
                                + File.separator
                                + "products";


                File sourceFolder =
                        new File(sourceDirectory);


                if (!sourceFolder.exists()) {
                    sourceFolder.mkdirs();
                }


                File sourceFile =
                        new File(
                                sourceFolder,
                                fileName
                        );


                image.transferTo(sourceFile);


                // Target directory

                String targetDirectory =
                        System.getProperty("user.dir")
                                + File.separator
                                + "target"
                                + File.separator
                                + "classes"
                                + File.separator
                                + "static"
                                + File.separator
                                + "uploads"
                                + File.separator
                                + "products";


                File targetFolder =
                        new File(targetDirectory);


                if (!targetFolder.exists()) {
                    targetFolder.mkdirs();
                }


                File targetFile =
                        new File(
                                targetFolder,
                                fileName
                        );


                java.nio.file.Files.copy(
                        sourceFile.toPath(),
                        targetFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );


                imagePath =
                        "/uploads/products/"
                                + fileName;


                System.out.println(
                        "UPDATED IMAGE: " +
                                imagePath
                );


            } catch (Exception e) {

                System.out.println(
                        "IMAGE UPDATE ERROR"
                );

                e.printStackTrace();


                response.put("success", false);
                response.put(
                        "message",
                        "Image upload failed."
                );

                return response;
            }
        }


        // -------------------------
        // UPDATE DATABASE
        // -------------------------

        boolean success =
                ProductDAO.updateProduct(
                        id,
                        name.trim(),
                        price,
                        quantity,
                        imagePath
                );


        response.put(
                "success",
                success
        );


        if (success) {

            response.put(
                    "message",
                    "Product updated successfully."
            );

            response.put(
                    "image",
                    imagePath
            );

        } else {

            response.put(
                    "message",
                    "Product not found."
            );
        }


        return response;
    }
}

