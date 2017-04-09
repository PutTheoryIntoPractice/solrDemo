import com.hover.bean.CatalogBean;
import com.hover.service.CataLogService;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
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
        doc.addField("iyear", "2017");
        doc.addField("iway", "ceshi");
        doc.addField("onsale", "1");

        cataLogService.addDoc(doc);
    }

    @Test
    public void testAddBean() throws Exception {
        CatalogBean bean = new CatalogBean();
        bean.setCatalogid("29");
        bean.setCatalogname("测试");
        bean.setFatherid("1");
        bean.setPhoto("/car.jpg");
        bean.setOnsale("1");
        bean.setIway("ceshi");
        bean.setIyear("2017");

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

    @Test
    public void testQuery() throws Exception {
        QueryResponse queryResponse = cataLogService.query();
        SolrDocumentList results = queryResponse.getResults();
        System.out.println(results.toString());
        System.out.println("total:" + results.getNumFound());
        for (SolrDocument solrDocument: results) {
            System.out.println("-------" + solrDocument.get("catalogid") + "-------");
            System.out.println("车型id:" + solrDocument.get("catalogid"));
            System.out.println("车型名称:" + solrDocument.get("catalogname"));
            System.out.println("车型fatherId:" + solrDocument.get("fatherid"));
            System.out.println("车型照片:" + solrDocument.get("photo"));
            System.out.println();
        }
    }

    @Test
    public void testFacet() throws Exception {
        //调用service获取solrclient的查询结果
        QueryResponse queryResponse = cataLogService.facet();
        //获取查询的结果总数
        System.out.println("------total:" + queryResponse.getResults().getNumFound());
        //获取一级类List
        List<FacetField> facetFieldList = queryResponse.getFacetFields();
        for (FacetField facetField: facetFieldList) {
            //输出一级类名称
            System.out.println("facetFeildName:" + facetField.getName() + "---count:" + facetField.getValues().size());
            List<FacetField.Count> countlist = facetField.getValues();
            for (FacetField.Count count: countlist) {
                //输出二级类名称和count值
                System.out.println(count.getName() + ":" + count.getCount());
            }
            System.out.println();
        }
    }

}
