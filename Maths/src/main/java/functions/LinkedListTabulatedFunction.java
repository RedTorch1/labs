package functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction{
    private Node head;
    protected int count; //число элементов
    private void addNode(double x, double y) { //добавление элемента в конец
        Node newNode = new Node(x,y);
        if (head==null) { //Пустой список становится циклическим из одного элемента
            head=newNode;
            head.next=head;
            head.prev=head;
        }
        else //Добавление в конец элемента и налаживание связей
        {
            Node last=head.prev;
            last.next=newNode;
            newNode.prev=last;
            newNode.next=head;
            head.prev=newNode;
        }
        count++;
    }
    //Конструктор с массивами значений иксов и игриков
    public LinkedListTabulatedFunction(double[] xValues, double[] yValues)
    {
        if (xValues.length!=yValues.length) {throw new IllegalArgumentException("Arrays must be same length");}
        if (xValues.length==0) {throw new IllegalArgumentException("Arrays must not be empty");}

        for(int i=1; i<xValues.length;i++) //Проверка xValues
        {
            if (xValues[i]<=xValues[i-1])
            {
                throw new IllegalArgumentException("xValues must be strictly increasing");
            }
        }
        for(int i=0;i<xValues.length;i++)
        {
            addNode(xValues[i],yValues[i]);
        }
    }
    //Конструктор с четыремя параметрами
    public LinkedListTabulatedFunction(MathFunction src, double xFrom, double xTo, int count)
    {
        if (count<=0) {throw new IllegalArgumentException("Count must be positive");}
        if(xFrom>xTo)
        {
            double temp = xFrom;
            xFrom=xTo;
            xTo=temp; //Меняем местами для удобства
        }
        if (xFrom==xTo) //точки одинаковые
        {
            for (int i=0;i<count;i++) {addNode(xFrom, src.apply(xFrom));}
        }
        else
        { //Равномерная дискретизация
            double step=(xTo-xFrom)/(count-1);
            for (int i=0;i<count;i++)
            {
                double x=xFrom+i*step;
                addNode(x,src.apply(x));
            }
        }
    }
    private Node getNode(int index) {
        if (index<0||index>=count) {throw new IndexOutOfBoundsException("Index: "+index+" , Size: "+count);}
        Node cur=head;
        for (int i=0;i<index;i++) {cur=cur.next;}
        return cur;
    }

    //Вот с этого момента реализация пошла
    public int getCount() {return count;}
    public double getX(int index) {return getNode(index).x;}
    public double getY(int index) {return getNode(index).y;}
    public void setY(int index, double value) {getNode(index).y=value;}
    public double leftBound() {
        if (head==null) {throw new IllegalStateException("List is empty");}
        return head.x;
    }
    public double rightBound() {
        if (head==null) {throw new IllegalStateException("List is empty");}
        return head.prev.x;
    }
    public int indexOfX(double x) {
        if (head==null) {return -1;}
        Node cur=head;
        for (int i=0; i<count;i++)
        {
            if(cur.x==x) {return i;}
            cur=cur.next;
        }
        return -1;
    }
    public int indexOfY(double y) {
        if (head==null) {return -1;}
        Node cur=head;
        for (int i=0;i<count;i++)
        {
            if(cur.y==y) {return i;}
            cur=cur.next;
        }
        return -1;
    }
    protected int floorIndexOfX(double x) {
        if (head==null) {throw new IllegalStateException("List is empty");}
        //Если все иксы меньше аргументного
        if (x>head.prev.x) {return count;}
        if (x<head.x) {return 0;} //наоборот
        Node cur=head;
        for(int i=0;i<count;i++) {
            if(cur.x>=x) {return i;}
            cur=cur.next;
        }
        return count;
    }
    protected double extrapolateLeft(double x) {
        if(count==1) {return head.y;}
        return interpolate(x,head.x,head.next.x,head.y,head.next.y);
    }
    protected double extrapolateRight(double x) {
        if(count==1) {return head.y;}
        Node last=head.prev;
        Node prevLast=last.prev;
        return interpolate(x,prevLast.x,last.x,prevLast.y,last.y);
    }
    protected double interpolate(double x,int floorIndex) {
        if(floorIndex<=0||floorIndex>=count) {throw new IllegalArgumentException("Invalid floorIndex: "+floorIndex);}
        Node leftNode=getNode(floorIndex-1);
        Node rightNode=getNode(floorIndex);
        return interpolate(x,leftNode.x,rightNode.x,leftNode.y,rightNode.y);
    }
}
