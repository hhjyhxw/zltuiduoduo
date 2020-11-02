$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'zltdd/zltddawards/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '获奖用户', name: 'user.nickname', index: 'user_id', width: 80 },
			{ label: '获奖方式', name: 'awardsType', width: 60, formatter: function(value, options, row){
                             return value == '0' ?'<span class="label label-danger">参与活动</span>' :
                              (value == '1' ?'<span class="label label-danger">下线扫码</span>' :'未知')
                                                }},
			{ label: '奖品名称', name: 'prizeName', index: 'prize_name', width: 80 },
			{ label: '赠送积分', name: 'scores', index: 'scores', width: 80 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
                 return value == '0' ?'<span class="label label-danger">未领取</span>' :
                  (value == '1' ?'<span class="label label-danger">已领取</span>' :
                  (value == '2' ?'<span class="label label-danger">已过期</span>' :'未知'))
                                    }},

			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '领取时间', name: 'receiveTime', index: 'receive_time', width: 80 }, 			
			{ label: '过期时间', name: 'expireTime', index: 'expire_time', width: 80 },
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
		zltddAwards: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.zltddAwards = {};
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
                var url = vm.zltddAwards.id == null ? "zltdd/zltddawards/save" : "zltdd/zltddawards/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.zltddAwards),
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
                        url: baseURL + "zltdd/zltddawards/delete",
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
			$.get(baseURL + "zltdd/zltddawards/info/"+id, function(r){
                vm.zltddAwards = r.zltddAwards;
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