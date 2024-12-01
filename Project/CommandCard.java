package Project;

public class CommandCard {
    private int id;

    public CommandCard(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void executeCard(/*, enum Color */){
        if (this.id == 0){
            //expand 
        } else if (this.id == 1){
            //explore
        } else {
            //invade
        }
    };

}
