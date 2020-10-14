package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.model.CanalClientInstanceConfig;
import com.alibaba.otter.canal.admin.model.NodeClient;
import com.alibaba.otter.canal.admin.rest.ClientAdapterApi;
import com.alibaba.otter.canal.admin.service.CanalClientInstanceService;
import com.alibaba.otter.canal.admin.service.CanalConfigService;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import feign.Feign;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * Canal Client实例配置信息业务层
 *
 * @author XuDaojie
 * @date 2020/10/13
 * @since 1.1.5
 */
@Service
public class CanalClientInstanceServiceImpl implements CanalClientInstanceService {

    @Resource
    private CanalConfigService canalConfigService;

    @Override
    public boolean remoteOperation(String instance, Long nodeId, String option) {
        NodeClient nodeClient = NodeClient.find.byId(nodeId);

//        "start" stop
        // todo 缓存api
        ClientAdapterApi api = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(ClientAdapterApi.class,
                        MessageFormat.format("http://{0}:{1}", nodeClient.getIp(), nodeClient.getPort()));
        List<Map<String, String>> destinations = api.destinations();
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
            // 解析application.yml获取instance
//            CanalConfig canalConfig = canalConfigService.getCanalConfig(nodeClient.getClusterId(), clientId);
//            String content = canalConfig.getContent();
//            Yaml yaml = new Yaml();
//            Map<String, Map<String, Object>> yamlMap = yaml.load(content);
//
//            Map<String, Object> canalConfigMap = yamlMap.get("canal.conf");
//            if (canalConfigMap != null) {
//                if (canalConfigMap.get("canalAdapters") instanceof List) {
//                    List<Map<String, Object>> canalAdapters = (List<Map<String, Object>>) canalConfigMap.get("canalAdapters");
//                    for (Map<String, Object> canalAdapter : canalAdapters) {
//                        CanalClientInstanceConfig canalClientInstanceConfig = new CanalClientInstanceConfig();
//                        canalClientInstanceConfig.setName(String.valueOf(canalAdapter.get("instance")));
//                    }
//                }
//            }

            ClientAdapterApi api = Feign.builder()
                    .encoder(new FormEncoder(new JacksonEncoder()))
                    .decoder(new JacksonDecoder())
                    .target(ClientAdapterApi.class,
                    MessageFormat.format("http://{0}:{1}", nodeClient.getIp(), nodeClient.getPort()));
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
