const {createApp} = Vue 

const app = createApp({
    data() {
        return {
            type: undefined,
            color: undefined,
            accountSelected: undefined,
            message: '',
            error: undefined,
            data: [],
            dataAccounts: []
        }
    },
    created() {
        this.loadData()
    }, 
    methods: {
        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.data = response.data,
                    this.dataAccounts = this.data.accounts
                })
        },
        createCards() {
            axios.post('/api/clients/current/cards', `tipo=${this.type}&color=${this.color}&account=${this.accountSelected}`)
                .then(() => {
                    Swal.fire(
                        'Card Created',
                        'Look at your new card!',
                        'success'
                    )
                    .then(() => location.href = "/web/cards.html")
                })
                .catch(response => {
                    this.error = response.response.status
                    if(this.error == 409) {
                        this.message = 'The color of the cards cannot be repeated'
                        setTimeout(() => this.message = '', 3000)
                    } else if(this.error == 403) {
                        this.message = 'Cannot create more cards'
                        setTimeout(() => this.message = '', 3000)
                    } else if(this.error == 404){
                        this.message = 'You must associate an account'
                        setTimeout(() => this.message = '', 3000)
                    } else {
                        this.message = 'You must complete all fields'
                        setTimeout(() => this.message = '', 3000)
                    }
                })
        },
        logOut() {
            axios.post('/api/logout')
                .then(() => location.href = '/web/index.html'  )
        }    
    }
})
app.mount('#content')