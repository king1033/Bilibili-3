package org.pqh.dao;

import org.pqh.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 10295 on 2016/5/22.
 */
@Component
public interface VstorageDao {
    void insertVstorage(Vstorage vstorage);

    void updateVstorage(Vstorage vstorage);

    void insertData(Data data);

    void updateData(Data data);

    List<Data> selectData(Map<String,String> map);

    List<Data> selectDataCid(Map<String,String> map);

    void insertFiles(Files files);

    void updateFiles(Files files);

    void insertDispatch_servers(Dispatch_servers dispatch_servers);

    void insertUpload(Upload upload);

    void insertNode_server(Node_server node_server);

    void insertUpload_meta(Upload_meta upload_meta);

}
