package com.example.book;

class CardItem {
    private String id;          // DB에 저장할 아이디
    private String title;       // 제목
    private String contents;    // 내용
    private String photoUrl;    // 사진 경로

    public CardItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public CardItem(String title, String contents, String photoUrl) {
        this.title = title;
        this.contents = contents;
        this.photoUrl = photoUrl;
    }

    public CardItem(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }




}
