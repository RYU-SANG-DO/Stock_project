/*
 * eGovFrame LDAP조직도관리
 * Copyright The eGovFrame Open Community (http://open.egovframe.go.kr)).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author 전우성(슈퍼개발자K3)
 */
package egovframework.stock.paxnet.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;

@Controller
public class StockPaxnetController {

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	/**
	 * PAXNET 주식 정보
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="PAXNET 주식 정보",order = 13000 ,gid = 200 ,keyL1="stock" ,keyL2="paxnet",lv=0)
    @RequestMapping("/stock/Paxnet/selectPaxnetList.do")
    public String selectPaxnetList() throws Exception {

        return "egovframework/com/ext/ldapumt/EgovDeptManageTree";
    }

   

}
