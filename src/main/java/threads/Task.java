package threads;

import functions.functions.Function;

public class Task{
    private Function function;
    private Double   leftBorder;
    private Double   rightBorder;
    private Double   discretization;
    private Integer  tasks;

    public Task(){}

    public Task( Function function, Double leftBorder, Double rightBorder, Double discretization ){
        this.function = function;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.discretization = discretization;
    }

    public Function getFunction(){
        return function;
    }

    public void setFunction( Function function ){
        this.function = function;
    }

    public Double getLeftBorder(){
        return leftBorder;
    }

    public void setLeftBorder( Double leftBorder ){
        this.leftBorder = leftBorder;
    }

    public Double getRightBorder(){
        return rightBorder;
    }

    public void setRightBorder( Double rightBorder ){
        this.rightBorder = rightBorder;
    }

    public Double getDiscretization(){
        return discretization;
    }

    public void setDiscretization( Double discretization ){
        this.discretization = discretization;
    }

    public Integer getTasks(){
        return tasks;
    }

    public void setTasks( Integer tasks ){
        this.tasks = tasks;
    }
}
