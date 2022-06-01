package swdm2016.gachon.mr_demo;

public class WritePostInfo {


    private String title;
    private String imageURI;
    private String content;
    private String publisher;
    private int index;


    public WritePostInfo(String title,String imageURI, String content,String publisher,int index){
        this.title = title;
        this.imageURI = imageURI;
        this.content = content;
        this.publisher = publisher;
        this.index = index;
    }
    public WritePostInfo(){

    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getImageURI(){
        return this.imageURI;
    }
    public void setImageURI(String imageURI){
        this.imageURI = imageURI;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public int getIndex(){
        return this.index;
    }
    public void setIndex(int index){
        this.index = index;
    }
}
