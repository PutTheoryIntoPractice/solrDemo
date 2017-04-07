import com.szp.bean.CatalogBean;
import com.szp.service.CataLogService;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SZP on 2017/4/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class TestCataLogService {
    @Autowired
    CataLogService cataLogService;

    @Test
    public void testAddDoc() throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("catalogid", 28);
        doc.addField("catalogname", "测试车型update");
        doc.addField("fatherid", 0);
        doc.addField("photo", "/img.jpg");

        cataLogService.addDoc(doc);
    }

    @Test
    public void testAddBean() throws Exception {
        CatalogBean bean = new CatalogBean();
        bean.setCatalogid("29");
        bean.setCatalogname("测试");
        bean.setFatherid("1");
        bean.setPhoto("/car.jpg");

        cataLogService.addBean(bean);
    }

    @Test
    public void testDelete() throws Exception {
        cataLogService.delete();
    }

    @Test
    public void testUpdate() throws Exception {
        cataLogService.updateSet(44, "catalogname", "测试");

        cataLogService.updateInc(44, "fatherid", 1);

        cataLogService.updateAdd(44, "photo", "/dasd.png");

        List<String> list = new ArrayList<String>();
        list.add("1.jpg");
        list.add("2.jpg");
        list.add("3.jpg");
        list.add("4.jpg");
        cataLogService.updateMultiValueAdd(44, "photo", list);

        cataLogService.closeClient();
    }

}
