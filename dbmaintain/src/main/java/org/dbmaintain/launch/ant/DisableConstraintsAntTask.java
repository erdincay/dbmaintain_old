/*
 * Copyright DbMaintain.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dbmaintain.launch.ant;

import org.dbmaintain.launch.task.DbMaintainTask;
import org.dbmaintain.launch.task.DisableConstraintsTask;

/**
 * Task that disables or drops all foreign key and not null constraints.
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class DisableConstraintsAntTask extends BaseDatabaseAntTask {

    @Override
    protected DbMaintainTask createDbMaintainTask() {
        return new DisableConstraintsTask(databases);
    }
}
