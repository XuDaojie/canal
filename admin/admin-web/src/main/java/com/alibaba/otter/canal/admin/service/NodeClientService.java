package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.model.Pager;

import java.util.List;

public interface NodeClientService {

    void save(NodeClient nodeClient);

    NodeClient detail(Long id);

    void update(NodeClient nodeClient);

    void delete(Long id);

    List<NodeClient> findAll(NodeClient nodeClient);

    Pager<NodeClient> findList(NodeClient nodeClient, Pager<NodeClient> pager);

    int remoteNodeStatus(String ip, Integer port);

    String remoteCanalLog(Long id);

    boolean remoteOperation(Long id, String option);
}
