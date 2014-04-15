##########################GO-LICENSE-START################################
# Copyright 2014 ThoughtWorks, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
##########################GO-LICENSE-END##################################

class Api::AgentsController < Api::ApiController
  include AgentBulkEditor

  def index
    agents = agent_service.agents
    agents_api_arr = agents.collect{|agent| AgentAPIModel.new(agent)}
    render :json => agents_api_arr.to_json
  end

  def delete
      agent_service.deleteAgents(current_user, result = HttpOperationResult.new, [params[:uuid]])
      render_operation_result(result)
  end

  def disable
    agent_service.disableAgents(current_user, result = HttpOperationResult.new, [params[:uuid]])
    render_operation_result(result)
  end

  def enable
    agent_service.enableAgents(current_user, result = HttpOperationResult.new, [params[:uuid]])
    render_operation_result(result)
  end

  def edit_agents
    render :text => bulk_edit.message()
  end
end