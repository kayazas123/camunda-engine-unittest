/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule();

    @Test
    @Deployment(resources = {"testProcess.bpmn"})
    public void retryInsteadOfIncidentOnServiceTask() throws Exception {
        test("testProcess");
    }

    @Test
    @Deployment(resources = {"testProcess2.bpmn"})
    public void retryInsteadOfIncidentOnThrowEvent() throws Exception {
        test("testProcess2");
    }

    private void test(final String key) throws InterruptedException {
        runtimeService().startProcessInstanceByKey(key);

        sleep(SECONDS.toMillis(30L));
        final List<Incident> incidents = runtimeService().createIncidentQuery().list();

        assertThat(incidents.size()).isEqualTo(0);
        assertThat(FailingJob.ExecutionCount).isGreaterThan(1);
    }

}
