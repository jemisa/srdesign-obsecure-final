package cs492.obsecurefinal.common;

public class Topic implements Comparable {
    private int id;
    private double probability;
    private String[] topWords;

    public Topic(){
    }

    public Topic(int i, double d, String[] s){
            id = i;
            probability = d;
            topWords = s;
    }

    public int getId(){
            return id;
    }

    public double getProbability(){
            return probability;
    }

    public String[] getTopWords(){
            return topWords;
    }

    public void setId(int i){
            id = i;
    }

    public void setProbability(double p){
            probability = p;

    }

    public void setTopWords(String[] s){
            topWords = s;
    }

    @Override
    public int compareTo(Object o) 
    {
        Topic t = (Topic)o;
        
        if(t.probability > this.probability)
            return -1;
        else if (t.probability < this.probability)
            return 1;
        else 
            return 0;
    }
}
