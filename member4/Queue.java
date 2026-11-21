//Name: Daynea Chambers 2400660
package member4;
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import managerpackage.ResourceManager;

public class Queue
{
    private QNode Front;
    private QNode readyFront;
    private QNode readyRear;
    private QNode Rear;
    
    public Queue()
    {
      Front=null;
      Rear=null;
      readyFront=null;
      readyRear=null;
    }
    public QNode getFront()
    {
      return Front;
    }
    public void setFront(QNode n)
    {
      Front=n;
    }
    public QNode getRear()
    {
      return Rear;
    }
    public void setRear(QNode r)
    {
      Rear=r;
    }
    
    public Process Dequeue()
    {
      if (Front==null)
      {
        return null;
      }

      Process removedData=Front.getData();
      
      if (Rear==Front)
      {
        Front=null;
        Rear=null;
      }
      else
      {
        Front=Front.getnextNode();
        Front.setPrevNode(null);
      }
      return removedData;
    }
    //Function adds a process to the waiting queue
    public void Enqueue(Process data)
    {
    QNode newNode=new QNode(data);
      if (Front==null)
      {
        Front=newNode;
        Rear=newNode;
      }
      else
      {
       Rear.setNextNode(newNode);
       newNode.setPrevNode(Rear);
       Rear=newNode;
      }
    }
    //Function adds a process to the ready queue
    public void EnqueueRQueue(Process data)
    {
      QNode newNode=new QNode(data);
        if (readyFront==null)
        {
          readyFront=newNode;
          readyRear=newNode;
        }
        else
        {
         readyRear.setNextNode(newNode);
         newNode.setPrevNode(readyRear);
         readyRear=newNode;
        }
    }
    
    //Function to add a process to a queue
    public void AddProcesstoQueue(Process p)
    {
        QNode newNode=new QNode(p);
        if(Front==null)
        {
          Front=newNode;
          Rear=newNode;
        }
        else
        {
          Rear.setNextNode(newNode);
          newNode.setPrevNode(Rear);
          Rear=newNode;
        }
    }
    //Function to view processes waiting on resources to be allocated
    public void ViewWaitingQueue()
    {
        if (Front==null)
        {
          System.out.println("The queue is empty.");
        }
        else
        {
          System.out.println("Processes in the Queue: ");
            QNode cur_pos=Front;
            while (cur_pos !=null)
            {
               System.out.println(cur_pos.getData());
               cur_pos=cur_pos.getnextNode();
            }  
        }
    
    }
    //Captures process removed from the queue
    public void processWaitingQueue(ResourceManager manager)
    {
      Process cur_process=Dequeue();
      if (cur_process == null)
      {
        System.out.println("The queue is empty.");
        return;
      }

      String resource=cur_process.getresourceRequired();
      if(manager.isAvailable(resource))
      {
        boolean allocated=manager.allocate(resource);
        if (allocated)
        {
          System.out.println("Allocated"+ resource);
          manager.release(resource);
        }
      }
      else
      {
        System.out.println(resource + "is busy");
        Enqueue(cur_process);
      }
    }

    public void ViewReadyQueue()
    {
      if (readyFront==null)
      {
        System.out.println("The ready queue is empty.");
      }
      else
      {
        System.out.println("Processes in the Ready Queue: ");
          QNode cur_pos=readyFront;
          while (cur_pos !=null)
          {
             System.out.println(cur_pos.getData());
             cur_pos=cur_pos.getnextNode();
          }
           
      }
    
    }
  
}

//Enqueue and add to ready queue
//Allocate resources to processes
