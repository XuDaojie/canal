package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.model.CanalClientInstanceConfig;
import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.rest.ClientAdapterApi;
import com.alibaba.otter.canal.admin.service.CanalClientInstanceService;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Canal Client实例配置信息业务层
 *
 * @author XuDaojie
 * @date 2020/10/13
 * @since 1.1.5
 */
@Service
public class CanalClientInstanceServiceImpl implements CanalClientInstanceService {

    @Override
    public boolean remoteOperation(String instance, Long nodeId, String option) {
        NodeClient nodeClient = NodeClient.find.byId(nodeId);

        String url = MessageFormat.format("http://{0}:{1}", nodeClient.getIp(), nodeClient.getPort());
        ClientAdapterApi api = ClientAdapterApi.apis.get(url);
        api.syncSwitch(instance, "start".equals(option) ? "on" : "off");

        return true;
    }

    @Override
    public boolean instanceOperation(Long id, String option) {
        return false;
    }

    @Override
    public List<CanalClientInstanceConfig> findInstanceByClientId(Long clientId) {
        List<CanalClientInstanceConfig> result = new ArrayList<>();

        NodeClient nodeClient = NodeClient.find.byId(clientId);

        if (nodeClient != null) {

            String url = MessageFormat.format("http://{0}:{1}", nodeClient.getIp(), nodeClient.getPort());
            ClientAdapterApi api = ClientAdapterApi.apis.get(url);
            List<Map<String, String>> destinations = api.destinations();
            for (Map<String, String> destination : destinations) {
                CanalClientInstanceConfig instance = new CanalClientInstanceConfig();
                instance.setNodeClient(nodeClient);
                instance.setName(destination.get("destination"));
                instance.setRunningStatus("on".equals(destination.get("status")) ? "1" : "0");
                result.add(instance);
            }
        }

        return result;
    }
}
