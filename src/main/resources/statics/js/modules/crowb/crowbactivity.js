$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'crowb/crowbactivity/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '名称', name: 'title', index: 'title', width: 80 }, 			
			{ label: '需要积分值', name: 'score', index: 'score', width: 80 }, 			
			{ label: '活动人数', name: 'total', index: 'total', width: 80 }, 			
			{ label: '已报名人数', name: 'signed', index: 'signed', width: 80 }, 			
			{ label: '活动开始时间', name: 'starttime', index: 'starttime', width: 80 }, 			
			{ label: '活动结束时间', name: 'endtime', index: 'endtime', width: 80 }, 			
			{ label: '停用标志 0表示停用，1表示启用(默认启用)', name: 'status', index: 'status', width: 80 }, 			
			{ label: '消息发送状态0未发送 1表示活动成功已发送 2表示众筹失败发送消息', name: 'sendStatus', index: 'send_status', width: 80 }, 			
			{ label: '活动介绍描述', name: 'description', index: 'description', width: 80 }, 			
			{ label: '活动规则描述', name: 'rule', index: 'rule', width: 80 }, 			
			{ label: '活动入口链接', name: 'link', index: 'link', width: 80 }, 			
			{ label: '报名成功后参与地址', name: 'attendAddress', index: 'attend_address', width: 80 }, 			
			{ label: '报名成功后参与时间', name: 'attendTime', index: 'attend_time', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '创建时间', name: 'modifyTime', index: 'modify_time', width: 80 }, 			
			{ label: '是否需要积分支付积分(0不需要 1需要 默认1)', name: 'isneedScore', index: 'isneed_score', width: 80 }			
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
		crowbActivity: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.crowbActivity = {};
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
                var url = vm.crowbActivity.id == null ? "crowb/crowbactivity/save" : "crowb/crowbactivity/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.crowbActivity),
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
                        url: baseURL + "crowb/crowbactivity/delete",
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
			$.get(baseURL + "crowb/crowbactivity/info/"+id, function(r){
                vm.crowbActivity = r.crowbActivity;
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