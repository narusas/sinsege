HTTP Protocol



��û(GET/POST)

GET /ep/map/infoMapView.jsp?code=1&sub1=10&sub2=3 HTTP/1.0\r\n
ParamName: Value.
..
..
\r\n



POST /ep/map/infoMapView.jsp HTTP/1.0
Content-Length: 15
ParamName: Value.
..
..
\r\n
?code=1&sub1=10&sub2=3





����
HTTP 200 OK\r\n 
Content-Length: 100\r\n
\r\n
asasdasdasdasdasdas





���ǰ���

GET /ep/map/infoMapView.jsp?code=1&sub1=10&sub2=3 HTTP/1.0\r\n
\r\n

HTTP 200 OK\r\n 
Content-Length: 100\r\n
Cookie: sessionid=1020304\r\n;
\r\n
asasdasdasdasdasdas



GET /ep/map/infoLocationView.jsp?code=1 HTTP/1.0\r\n
Cookie: sessionid=1020304\r\n; 
\r\n




��Ű�� �ִ� �������� ����ϴ� �������� �ٸ��� ����. �ʿ�� �ϴ� ��Ű�� �ִ� �������� ���� �湮�ϰ� �;� �Ǵ� ��찡 ����. 

