package Project;

public class CommandCard {
    private int id;

    public CommandCard(int id){
        this.id = id;
    }

    public void executeCard(int idCard){
        if (id == 0){
            //expand
        } else if (id == 1){
            //explore
        } else {
            //invade
        }
    };

}
