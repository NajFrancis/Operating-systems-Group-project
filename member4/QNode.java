//Name: Daynea Chambers
//2400660
package member4;

public class QNode
{
    private Process data;
    private QNode nextNode;
    private QNode prevNode;
    
    public QNode()
    {
      this.data=new Process();
      this.nextNode=null;
      this.prevNode=null; 
    }
  
    public QNode(Process p)
    {
      this.data=p;
      this.nextNode=null;
      this.prevNode=null;
    }
    public QNode getnextNode()
    {
      return nextNode;
    }
    public void setNextNode(QNode q)
    {
      this.nextNode=q;
    }
    public QNode getPrevNode()
    {
      return prevNode;
    }
    public void setPrevNode(QNode q)
    {
      this.prevNode=q;
    }
    public Process getData()
    {
      return data;
    }
    public void setData(Process data)
    {
    this.data=data;
    }
}
