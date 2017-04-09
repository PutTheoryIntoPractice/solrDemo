package com.hover.service;

import com.hover.bean.CatalogBean;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.FacetParams;
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

    /**
     * solr查询数据
     * @return
     * @throws Exception
     */
    public QueryResponse query() throws Exception {
        SolrQuery query = new SolrQuery();
        //q 查询字符串，如果查询所有*:*
        query.set("q", "catalogid:*");
        //fq 过滤条件，过滤是基于查询结果中的过滤
        //query.set("fq", "catalogname:*驰*");
        //fq 此过滤条件可以同时搜索出奔驰和宝马两款车型，但是需要给catalogname配置相应的分词器
        //query.set("fq", "catalogname:奔驰宝马");
        //sort 排序，请注意，如果一个字段没有被索引，那么它是无法排序的
		query.set("sort", "catalogid desc");
        //start row 分页信息，与mysql的limit的两个参数一致效果
        query.setStart(0);
        query.setRows(100);
        //fl 指定返回那些字段内容，用逗号或空格分隔多个
		//query.set("fl", "catalogid,photo");

        return client.query(query);
    }

    /**
     * facet：solr多级分类功能
     * facet是solr的一个重要特性，可以一次实现根据多个字段的分类统计
     * @return
     * @throws Exception
     */
    public QueryResponse facet() throws Exception {
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");//必须有查询条件，否则一下操作都是没有意义的，会出现null的情况
        //query.set("fq", "catalogname:*驰*");//过滤条件是建立在上述查询条件的基础之上的
        query.set("fq", "-iyear:0");//不等于过滤条件
        query.setFacet(true);//设置facet=true,默认为true
        //这里设置的开始index和行数是没有作用的
        //query.setStart(0);
        //query.setRows(10);
        query.addFacetField(new String[] {"iyear", "iway", "onsale"});//设置需要facet的字段
        query.setFacetLimit(5);//限制facet返回的数量
        //query.setFacetSort("index");//默认是按count来排序的
        query.setFacetSort(FacetParams.FACET_SORT_COUNT);

        return client.query(query);
    }

}
