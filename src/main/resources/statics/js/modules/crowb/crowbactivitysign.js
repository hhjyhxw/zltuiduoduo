$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'crowb/crowbactivitysign/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '活动id', name: 'crowbActivityId', index: 'crowb_activity_id', width: 80 }, 			
			{ label: '姓名', name: 'name', index: 'name', width: 80 }, 			
			{ label: '手机', name: 'phone', index: 'phone', width: 80 }, 			
			{ label: '报名积分', name: 'score', index: 'score', width: 80 }, 			
			{ label: '报名时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: 'openid', name: 'openid', index: 'openid', width: 80 }, 			
			{ label: '昵称', name: 'nickname', index: 'nickname', width: 80 }, 			
			{ label: '是否带同伴', name: 'takePartner', width: 60, formatter: function(value, options, row){
                                                     return value == '0' ?'<span class="label label-success">不带</span>' :
                                                      (value == '1' ?'<span class="label label-success">带同</span>' :
                                                      (value == '2' ?'<span class="label label-success">未知</span>' :'未知'))
                                                                        }},
			{ label: '会员手机', name: 'memerberPhone', index: 'memerber_phone', width: 80 }, 			
			{ label: '是否有赞助意向', name: 'suportintention', width: 60, formatter: function(value, options, row){
                                                                 return value == '0' ?'<span class="label label-success">否</span>' :
                                                                  (value == '1' ?'<span class="label label-success">是</span>' :
                                                                  (value == '2' ?'<span class="label label-success">未知</span>' :'未知'))
                                                                                    }},
			{ label: '活动名称', name: 'activityName', index: 'activity_name', width: 80 }, 			
			{ label: '单位名称', name: 'unitName', index: 'unit_name', width: 80 }, 			
			{ label: '职务名称', name: 'officeName', index: 'office_name', width: 80 }, 			
			{ label: '携带同伴人数', name: 'takeNum', index: 'take_num', width: 80 }, 			
			{ label: '报名状态', name: 'verifyStatus', width: 60, formatter: function(value, options, row){
                                                                             return value == '0' ?'<span class="label label-success">未报名</span>' :
                                                                              (value == '1' ?'<span class="label label-success">已报名</span>' :
                                                                              (value == '2' ?'<span class="label label-success">审核中或者取消</span>' :
                                                                              (value == '3' ?'<span class="label label-success">审核失败</span>' :'未知')))
                                                                                                }},
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#icloudapp',
	data:{
		showList: true,
		title: null,
		crowbActivitySign: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.crowbActivitySign = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.crowbActivitySign.id == null ? "crowb/crowbactivitysign/save" : "crowb/crowbactivitysign/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.crowbActivitySign),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "crowb/crowbactivitysign/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "crowb/crowbactivitysign/info/"+id, function(r){
                vm.crowbActivitySign = r.crowbActivitySign;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});