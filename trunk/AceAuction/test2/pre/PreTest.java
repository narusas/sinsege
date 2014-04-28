package pre;

import java.util.List;

import org.junit.Test;

public class PreTest {

	@Test
	public void 담당계목록얻기() {
		PreFetcher f = new PreFetcher();
		List<담당계> list = f.fetch담당계("서울중앙지방법원");
		System.out.println(list);
	}

}
