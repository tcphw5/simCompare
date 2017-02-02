package WordCountHadoop;

public class Point{
	String name;
    int staytime;
    int traveltime;
    int encoding;
    public Point(){
    	name = "";
		staytime =0;
		traveltime=0;
		encoding = 0;
    }
	public Point(Point point) {
		name = point.name;
		staytime = point.staytime;
		traveltime = point.traveltime;
		encoding = point.encoding;
		// TODO Auto-generated constructor stub
	}
}
