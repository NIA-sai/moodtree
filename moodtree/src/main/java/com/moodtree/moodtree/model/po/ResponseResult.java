package com.moodtree.moodtree.model.po;

import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;

public class ResponseResult {
    private String history_id;
    private String conversation_id;
    private List<Part> output;
    private String status;

    // history_id的getter和setter
    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    // conversation_id的getter和setter
    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    // output的getter和setter
    public List<Part> getOutput() {
        return output;
    }

    public void setOutput(List<Part> output) {
        this.output = output;
    }

    // status的getter和setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Part {
        private String role;
        private Content content;
        private String status;
        private String created_at;
        private Map<String, Object> meta_data;

        // role的getter和setter
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        // content的getter和setter
        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        // status的getter和setter
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // created_at的getter和setter
        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        // meta_data的getter和setter
        public Map<String, Object> getMeta_data() {
            return meta_data;
        }

        public void setMeta_data(Map<String, Object> meta_data) {
            this.meta_data = meta_data;
        }
    }

    public static class Content {
        private String type;
        private String text;
        private List<Image> image;

        // type的getter和setter
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        // text的getter和setter
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        // image的getter和setter
        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }
    }

    public static class Image {
        private String image_url;

        // image_url的getter和setter
        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}

