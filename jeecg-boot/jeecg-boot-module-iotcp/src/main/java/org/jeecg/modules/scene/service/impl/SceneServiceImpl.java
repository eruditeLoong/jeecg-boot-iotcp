package org.jeecg.modules.scene.service.impl;

import org.jeecg.modules.scene.entity.Scene;
import org.jeecg.modules.scene.entity.SceneScheme;
import org.jeecg.modules.scene.mapper.SceneSchemeMapper;
import org.jeecg.modules.scene.mapper.SceneMapper;
import org.jeecg.modules.scene.service.ISceneService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 场景管理
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Service
public class SceneServiceImpl extends ServiceImpl<SceneMapper, Scene> implements ISceneService {

	@Autowired
	private SceneMapper sceneMapper;
	@Autowired
	private SceneSchemeMapper sceneSchemeMapper;
	
}
