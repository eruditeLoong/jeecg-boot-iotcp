package jeecg;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.device.entity.*;
import org.jeecg.modules.device.service.*;
import org.jeecg.modules.device.vo.DeviceInstanceDetail;
import org.jeecg.modules.message.entity.DataReport;
import org.jeecg.modules.message.service.IDataReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleTest {

	@Autowired
	private IDeviceInstanceService deviceInstanceService;
	@Autowired
	private IDeviceModelService deviceModelService;
	@Autowired
	private IDeviceDataService deviceDataService;
	@Autowired
	private IDeviceFunctionsService deviceFunctionsService;
	@Autowired
	private IDeviceEventService deviceEventService;
	@Autowired
	private IDeviceLabelService deviceLabelService;
	@Autowired
	private IDataReportService dataReportService;

	@Test
	public void testInstanceDetail() {

		String deviceInstanceId = "1252592652282937345";
		DeviceInstanceDetail detail = new DeviceInstanceDetail();
		// 设备实例
		DeviceInstance deviceInstance = deviceInstanceService.getById(deviceInstanceId);
		detail.setDeviceInstance(deviceInstance);
		// 模型信息
		DeviceModel deviceModel = deviceModelService.getById(deviceInstance.getModelBy());
		detail.setDeviceModel(deviceModel);

		// 设备数据节点
		List<DeviceData> deviceDataList = deviceDataService.lambdaQuery()
				.eq(DeviceData::getDeviceModelBy, deviceModel.getId())
				// .like(DeviceData::getRwAuthor, "r")
				.list();
		for (DeviceData deviceData : deviceDataList) {
			JSONObject jsonObject = JSONObject.parseObject(deviceData.getValueType());
			List<DataReport> dataReportList = dataReportService.lambdaQuery()
					.eq(DataReport::getInstanceDeviceBy, deviceInstanceId)
					.like(DataReport::getData, deviceData.getCode())
					.orderByDesc(DataReport::getCreateTime)
					.list();
			if (dataReportList != null && dataReportList.size() > 0) {
				DataReport dataReport = dataReportList.get(0);
				String value = JSONObject.parseObject(dataReport.getData()).getString(deviceData.getCode());
				jsonObject.put("value", value);
				jsonObject.put("reportTime", DateUtils.formatDate(dataReport.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			deviceData.setValueType(jsonObject.toJSONString());
		}
		detail.setDeviceDataList(deviceDataList);

		// 设备功能
		List<DeviceFunction> deviceFunctionList = deviceFunctionsService.lambdaQuery()
				.eq(DeviceFunction::getDeviceModelBy, deviceModel.getId())
				.list();
		detail.setDeviceFunctionList(deviceFunctionList);

		// 设备事件
		List<DeviceEvent> deviceEventList = deviceEventService.lambdaQuery()
				.eq(DeviceEvent::getDeviceModelBy, deviceModel.getId())
				.list();
		detail.setDeviceEventList(deviceEventList);

		// 上报数据
		List<DataReport> dataReportList = dataReportService.lambdaQuery()
				.eq(DataReport::getInstanceDeviceBy, deviceInstanceId)
				.list();
		detail.setDataReportList(dataReportList);

		System.out.println(detail.toString());
	}

}
