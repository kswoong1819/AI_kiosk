package com.web.blog.model.images;

public class Images {
    private String fileBase64;
    
    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public Images(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

}
