package com.moodtree.moodtree.model.po;

import java.util.List;

public class ResponseResult {
    String message;
    ResultInResponseResult result;
    private String status;//"0"

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ResultInResponseResult getResult() {
        return result;
    }
    public void setResult(ResultInResponseResult result) {
        this.result = result;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultInResponseResult {

        private List<Part> output;
        private String status;//"finish"

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
            private List<Content> content;

            // content的getter和setter
            public List<Content> getContent() {
                return content;
            }

            public void setContent(List<Content> content) {
                this.content = content;
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

            public List<Image> getImage() {
                return image;
            }

            public void setImage(List<Image> image) {
                this.image = image;
            }

            public static class Image
            {
                private String image_url;

                // image_url的getter和setter
                public String getImage_url ()
                {
                    return image_url;
                }

                public void setImage_url ( String image_url )
                {
                    this.image_url = image_url;
                }
            }
        }
    }
}

