import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(
		name = "test_servlet",
		urlPatterns = "/test_servlet"
)
public class TestServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Point<String, Integer>> points = new ArrayList<Point<String, Integer>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		
		
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			points.add(new Point<String, Integer>(sdf.format(calendar.getTime()), random.nextInt() % 1000));
			calendar.add(Calendar.HOUR, 24);
		}
		response.getWriter().print(new Gson().toJson(points));
	}
}
