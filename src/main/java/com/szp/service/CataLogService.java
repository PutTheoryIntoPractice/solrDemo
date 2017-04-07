package com.szp.service;

import com.szp.bean.CatalogBean;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SZP on 2017/4/6.
 */
@Service
public class CataLogService {

    @Resource(name = "catalogHttpSolrClient")
    private SolrClient client;

    public void closeClient() throws Exception {
        client.commit();
        client.close();
    }

    public void addDoc(SolrInputDocument doc) throws Exception {
        client.add(doc);
        client.commit();
        client.close();
    }

    public void addBean(CatalogBean bean) throws Exception {
        client.addBean(bean);
        client.commit();
        client.close();
    }

    public void delete() throws Exception {
        //1.删除一个
        //updateResponse = client.deleteById("0");

        //2.删除多个
//        List<String> ids = new ArrayList<String>();
//        ids.add("10");
//        ids.add("11");
//        updateResponse = client.deleteById(ids);

        //3.根据查询条件删除数据,这里的条件只能有一个，不能以逗号相隔
        //updateResponse = client.deleteByQuery("fatherid:0");
//
//        //4.删除全部，删除不可恢复
        client.deleteByQuery("*:*");
        client.commit();
        client.close();
    }

    /**
     * 更新某一个属性的值
     * @param id
     * @param field
     * @param value
     * @throws Exception
     */
    public void updateSet(int id, String field, String value) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        HashMap<String, Object> operation = new HashMap<String, Object>();
        operation.put("set", value);
        doc.addField(field, operation);
        doc.addField("catalogid", id);
        client.add(doc);
    }

    /**
     * 数值型属性累加操作
     * @param id
     * @param field
     * @param value
     * @throws Exception
     */
    public void updateInc(int id, String field, int value) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        HashMap<String, Object> operation = new HashMap<String, Object>();
        operation.put("inc", value);
        doc.addField(field, operation);
        doc.addField("catalogid", id);
        client.add(doc);
    }

    /**
     * 多值属性附加一个值
     * @param id
     * @param field
     * @param value
     * @throws Exception
     */
    public void updateAdd(int id, String field, String value) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        HashMap<String, Object> operation = new HashMap<String, Object>();
        operation.put("add", value);
        doc.addField(field, operation);
        doc.addField("catalogid", id);
        client.add(doc);
    }

    /**
     * field多值属性附加多个值
     * @param id
     * @param field
     * @param list
     * @throws Exception
     */
    public void updateMultiValueAdd(int id, String field, List<String> list) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        Map<String,List<String>> photos=new HashMap<String, List<String>>();
        photos.put("add", list);
        doc.addField(field, photos);
        doc.addField("catalogid", id);
        client.add(doc);
    }

    public void  getCatalog() throws Exception {
        //创建查询对象
        SolrQuery query = new SolrQuery();
        //q 查询字符串，如果查询所有*:*
        query.set("q", "catalogid:*");
        //fq 过滤条件，过滤是基于查询结果中的过滤
        //query.set("fq", "product_catalog_name:幽默杂货");
        //sort 排序，请注意，如果一个字段没有被索引，那么它是无法排序的
//		query.set("sort", "product_price desc");
        //start row 分页信息，与mysql的limit的两个参数一致效果
        query.setStart(0);
        query.setRows(10);
        //fl 查询哪些结果出来，不写的话，就查询全部，所以我这里就不写了
//		query.set("fl", "");
        //df 默认搜索的域
        //query.set("df", "product_keywords");

        //======高亮设置===
        //开启高亮
        query.setHighlight(true);
        //高亮域
        query.addHighlightField("catalogid");
        //前缀
        query.setHighlightSimplePre("<span style='color:red'>");
        //后缀
        query.setHighlightSimplePost("</span>");


        //执行搜索
        QueryResponse queryResponse = client.query(query);
        //搜索结果
        SolrDocumentList results = queryResponse.getResults();
        //查询出来的数量
        long numFound = results.getNumFound();
        System.out.println("总查询出:" + numFound + "条记录");

        //遍历搜索记录
        //获取高亮信息
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : results) {
            System.out.println("车型id:" + solrDocument.get("catalogid"));
            System.out.println("车型名称 :" + solrDocument.get("catalogname"));
            System.out.println("车型fatherId:" + solrDocument.get("fatherid"));
            System.out.println("车型照片:" + solrDocument.get("photo"));

            //输出高亮信息
            Map<String, List<String>> map = highlighting.get(solrDocument.get("catalogid"));
            List<String> list = map.get("catalogid");
            if(list != null && list.size() > 0){
                System.out.println(list.get(0));
            }
        }

        client.close();
    }

}
