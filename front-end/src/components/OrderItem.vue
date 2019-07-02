<template>
    <tr>
        <td class="align-middle">{{ order.date }}</td>
        <td class="align-middle">{{ order.buyerName }} [Uid:{{ order.buyerUid }}]</td>
        <td class="align-middle">
            <div v-for="(book, index) in order.books" v-bind:key="index">
                {{ author(book.author) }} - {{ title(book.title) }} [ISBN:{{ book.isbn }}]，单价：{{ book.price }}，购买数量：{{ book.count }}
            </div>
        </td>
        <td>￥{{ totalPrice }}</td>
    </tr>
</template>

<script>
    export default {
        name: 'OrderItem',
        props: ['order'],
        computed: {
            totalPrice: function () {
                var i = 0, sum = 0, items = this.order.books;
                for(i = 0; i < items.length; i++){
                    sum += items[i].price * items[i].count;
                }
                return sum;
            }
        },
        methods: {
            author: function (value) {
                if(value.length === 0){
                    return '佚名';
                }
                return value;
            },
            title: function (value) {
                if(value.length === 0){
                    return '无题';
                }
                return value;
            }
        }
    }
</script>

<style scoped>

</style>
