package swdm2016.gachon.mr_demo;

public class MemberInfo {


    private String name;
    private String height;
    private String weight;
    private String age;
    private int count;

    public MemberInfo(String name,String height,String weight,String age,int count){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.count = count;
    }
    public MemberInfo(){

    }


    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }


    public String getHeight(){
        return this.height;
    }
    public void setHeight(String height){
        this.height = height;
    }

    public String getWeight(){
        return this.weight;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }

    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age = age;
    }

    public int getCount(){
        return this.count;
    }
    public void setCount(int count){
        this.count = count;
    }
}
