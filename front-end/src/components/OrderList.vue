<template>
    <div>
        <div class="bg-primary text-white p-3 mb-3">订单管理</div>
        <!-- Filters -->
        <div>
            <div class="align-middle pb-3 mb-3 bg-secondary text-light">
                <div class="row input-group pt-3">
                    <div class="col-2">
                        <input type="datetime-local" class="form-control" title="起始时间"
                               v-model="filters.time.start" v-bind:disabled="!filters.time.enabled"/>
                    </div>
                    <span class="col-1 m-auto">到</span>
                    <div class="col-2">
                        <input type="datetime-local" class="form-control" title="结束时间"
                               v-model="filters.time.end" v-bind:disabled="!filters.time.enabled"/>
                    </div>
                    <div class="col-1 offset-5">
                        启用该选项
                    </div>
                    <div class="col-1">
                        <input type="checkbox" value="time_filter_enabled" class="form-control"
                               v-model="filters.time.enabled"/>
                    </div>
                </div>
                <div v-if="admin" class="input-group bg-secondary pt-2">
                    <input type="text" class="form-control col-2" placeholder="按订单号搜索" title="订单号"
                           v-model.number="filters.orderid.value" v-bind:disabled="!filters.orderid.enabled"/>
                    <div class="col-1 offset-8">
                        启用该选项
                    </div>
                    <div class="col-1">
                        <input type="checkbox" value="time_filter_enabled" class="form-control"
                               v-model="filters.orderid.enabled"/>
                    </div>
                </div>
                <div v-if="admin" class="input-group bg-secondary pt-2">
                    <input type="text" class="form-control col-2" placeholder="按用户UID搜索" title="UID"
                           v-model.number="filters.uid.value" v-bind:disabled="!filters.uid.enabled"/>
                    <div class="col-1 offset-8">
                        启用该选项
                    </div>
                    <div class="col-1">
                        <input type="checkbox" value="time_filter_enabled" class="form-control"
                               v-model="filters.uid.enabled"/>
                    </div>
                </div>
                <div v-if="admin" class="input-group bg-secondary pt-2">
                    <input type="text" class="form-control col-2" placeholder="按图书ISBN搜索" title="ISBN"
                           v-model="filters.isbn.value" v-bind:disabled="!filters.isbn.enabled"/>
                    <div class="col-1 offset-8">
                        启用该选项
                    </div>
                    <div class="col-1">
                        <input type="checkbox" value="time_filter_enabled" class="form-control"
                               v-model="filters.isbn.enabled"/>
                    </div>
                </div>
                <hr/>
                <button class="btn btn-block btn-info col-8 offset-2 p-2" v-on:click="requestOrderList()">
                    查询
                </button>
                <div v-if="!gettingOrderList&&!getOrderListFail" class="text-center mt-3">
                    <div v-if="orders.length>0">
                        共 {{ orders.length }} 条结果<br/>
                        <span v-if="!oldFilters.orderid.enabled">
                            <span v-if="oldFilters.time.enabled">（指定时间段内）<br/></span>
                            <span v-if="oldFilters.isbn.enabled&&oldFilters.uid.enabled">该用户购买本书数量：{{ totalAmount }}<br/></span>
                            <span v-else-if="oldFilters.isbn.enabled">
                                本书销量：{{ totalAmount }}
                            </span>
                            <span v-else-if="oldFilters.uid.enabled">
                                该用户总消费：￥{{ totalCost }}<br/>
                                单次消费最高：￥{{ highestCost }}
                            </span>
                            <span v-else>
                                总销售额：￥{{ totalCost }}<br/>
                                最畅销：{{ bestSeller.bookTitle }}（ISBN：{{ bestSeller.bookIsbn }}，售出 {{ bestSeller.amount }} 本）
                            </span>
                        </span>
                    </div>
                    <span v-else>（无结果）</span>
                </div>
            </div>
        </div>
        <!-- Table -->
        <div v-if="gettingOrderList">获取订单信息中……</div>
        <div v-else-if="getOrderListFail">获取订单信息失败……</div>
        <div v-else>
            <table class="table">
                <thead>
                <tr>
                    <th>时间</th>
                    <th>买家</th>
                    <th>购买项</th>
                    <th>总价</th>
                </tr>
                </thead>
                <tbody>
                <OrderItem v-for="(order, index) in orders" v-bind:key="index" v-bind:order="order"></OrderItem>
                </tbody>
                <tfoot></tfoot>
            </table>
        </div>
    </div>
</template>

<script>
    import OrderItem from '@/components/OrderItem';
    export default {
        name: 'OrderList',
        components: {OrderItem},
        props: {
            admin: {
                default: false
            }
        },
        created: function () {
            if(!this.admin){
                // compulsory
                this.filters.uid.enabled = true;
                this.oldFilters.uid.enabled = true;
            }
            this.requestOrderList();
        },
        computed: {
            totalCost: function () {
                let sum = 0, i = 0, j = 0;
                for(i = 0; i < this.orders.length; i++){
                    let order = this.orders[i].books;
                    for(j = 0; j < order.length; j++){
                        sum += order[j].price * order[j].count;
                    }
                }
                return sum;
            },
            highestCost: function () {
                let max = 0, i = 0, j = 0;
                for(i = 0; i < this.orders.length; i++){
                    let order = this.orders[i].books;
                    let cost = 0;
                    for(j = 0; j < order.length; j++){
                        cost += order[j].price * order[j].count;
                    }
                    if (cost > max) {
                        max = cost;
                    }
                }
                return max;
            },
            totalAmount: function () {
                let amount = 0, i = 0, j = 0;
                for(i = 0; i < this.orders.length; i++){
                    let order = this.orders[i].books;
                    for(j = 0; j < order.length; j++){
                        if(order[j].isbn === this.oldFilters.isbn.value){
                            amount += order[j].count;
                        }
                    }
                }
                return amount;
            }
        },
        methods: {
            requestOrderList: function () {
                this.gettingOrderList = true;
                this.$http.post(
                    (this.admin ? 'admin/order' : 'order'), this.filters
                ).then(
                    (resp)=>{
                        this.orders = resp.data.orders;
                        // deep copy
                        this.oldFilters = {
                            uid: {
                                enabled: false,
                                value: ''
                            },
                            orderid: {
                                enabled: false,
                                value: ''
                            },
                            isbn: {
                                enabled: false,
                                value: ''
                            },
                            time: {
                                enabled: false,
                                start: new Date(),
                                end: new Date()
                            }
                        };
                        this.oldFilters.uid.enabled = this.filters.uid.enabled;
                        this.oldFilters.uid.value = this.filters.uid.value;
                        this.oldFilters.orderid.enabled = this.filters.orderid.enabled;
                        this.oldFilters.orderid.value = this.filters.orderid.value;
                        this.oldFilters.isbn.enabled = this.filters.isbn.enabled;
                        this.oldFilters.isbn.value = this.filters.isbn.value;
                        this.oldFilters.time.enabled = this.filters.time.enabled;
                        this.oldFilters.time.start = this.filters.time.start;
                        this.oldFilters.time.end = this.filters.time.end;
                        this.bestSeller = this.getBestSeller();
                        this.getOrderListFail = false;
                        this.gettingOrderList = false;
                    }, ()=>{
                        this.getOrderListFail = true;
                        this.gettingOrderList = false;
                    }
                );
            },
            getBestSeller: function () {
                let map = new Map();
                let amount = new Map();
                let i = 0, j = 0;
                for(i = 0; i < this.orders.length; i++){
                    let order = this.orders[i].books;
                    for(j = 0; j < order.length; j++){
                        if(!map.has(order[j].isbn)){
                            map.set(order[j].isbn, order[j].title);
                        }
                        if(!amount.has(order[j].isbn)){
                            amount.set(order[j].isbn, 0);
                        }
                        amount.set(order[j].isbn, amount.get(order[j].isbn) + order[j].count);
                    }
                }
                let maxAmount = 0, maxIsbn = '';
                for(let entry of amount){
                    let isbn = entry[0];
                    let amt = entry[1];
                    // more recent best sellers count more than those earlier
                    if(maxAmount <= amt){
                        maxAmount = amt;
                        maxIsbn = isbn;
                    }
                }
                return {
                    bookIsbn: maxIsbn,
                    bookTitle: map.get(maxIsbn),
                    amount: maxAmount
                };
            }
        },
        data: function () {
            return {
                oldFilters: {
                    uid: {
                        enabled: false,
                        value: ''
                    },
                    orderid: {
                        enabled: false,
                        value: ''
                    },
                    isbn: {
                        enabled: false,
                        value: ''
                    },
                    time: {
                        enabled: false,
                        start: new Date(),
                        end: new Date()
                    }
                },
                filters: {
                    uid: {
                        enabled: false,
                        value: ''
                    },
                    orderid: {
                        enabled: false,
                        value: ''
                    },
                    isbn: {
                        enabled: false,
                        value: ''
                    },
                    time: {
                        enabled: false,
                        start: new Date(),
                        end: new Date()
                    }
                },
                orders: [],
                gettingOrderList: false,
                getOrderListFail: false,
                bestSeller: {}
            };
        }
    }
</script>

<style scoped>

</style>
