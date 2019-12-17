package meowland.database;

public class Costs
{
    private String reason, info;
    private int sum;

    public Costs(String reason, String info, int sum)
    {
        this.reason = reason;
        this.info = info;
        this.sum = sum;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public int getSum()
    {
        return sum;
    }

    public void setSum(int sum)
    {
        this.sum = sum;
    }
}
