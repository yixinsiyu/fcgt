package net.engining.gateway.fts.invoker.sccc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import net.engining.control.core.flow.FlowContext;
import net.engining.control.core.invoker.Invoker;
import net.engining.control.core.invoker.InvokerDefinition;
import net.engining.control.core.invoker.Skippable;
import net.engining.gateway.fts.sao.sccc.AccountingSao;
import net.engining.gateway.fts.sdk.bean.EveryDayAccountingBean;
import net.engining.gateway.fts.sdk.bean.resultBack.ResponseData;
import net.engining.gateway.fts.sdk.key.EveryDayAccountingBeanKey;
import net.engining.gateway.fts.sdk.key.ResponseDataKey;

@InvokerDefinition(
		name = "冲正罚息计提", 
		requires = {
				}, 
		results = { 
				ResponseDataKey.class 
				}
		)
public class CorrectionPnitAccPostTxn implements Invoker, Skippable {

	private static final Logger log = LoggerFactory.getLogger(CorrectionPnitAccPostTxn.class);

	@Autowired
	AccountingSao accountingSao;
	
	@Override
	public boolean skippable(Map<String, String> parameters) {
		String skip = parameters.get(FlowContext.CONS_PARAMETERS.SKIP.toString());
		if("true".equals(skip)){
			return true;
		}
		if("Yes".equals(skip)){
			return true;
		}
		return false;
	}

	@Override
	public void invoke(FlowContext ctx) {
		EveryDayAccountingBean correctionAccount = ctx.get(EveryDayAccountingBeanKey.class);
		//TODO
		//调用微服务
		ResponseData response = accountingSao.correctionPnitAccTxn(correctionAccount);
		
		ctx.put(ResponseDataKey.class, response);
		String result = JSON.toJSONString(response);
		log.info("冲正罚息计提的结果为："  + result);
	}

}
