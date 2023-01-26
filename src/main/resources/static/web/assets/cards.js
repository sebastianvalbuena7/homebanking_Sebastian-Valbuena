const {createApp} = Vue 

const app = createApp({
    data() {
        return {
            data: [],
            dataCards: [],
            cardsDebit: [],
            cardsCredit: [],
            filterCard: [],
            dateString: ''
        }
    },
    created() {
        this.loadCards()
    },
    methods: {
        loadCards() {
            axios.get("/api/clients/current")
                .then(result => {
                    this.data = result.data
                    this.dataCards = this.data.cards
                    this.dateString = new Date().getFullYear().toString()
                    this.filterCard = this.dataCards.filter(card => card.showCard)
                    this.filterCards(this.filterCard)
                })
        },
        filterCards(cards) {
            this.cardsCredit = cards.filter(card => card.type === 'CREDIT')
            this.cardsDebit = cards.filter(card => card.type === 'DEBIT')
        },
        deleteCard(id) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post(`/api/clients/current/cards/${id}`)
                    .then(() => { 
                        location.href = '/web/cards.html'
                    })
                    .catch(error => console.log(error))
                  Swal.fire(
                    'Deleted!',
                    'Your file has been deleted.',
                    'success'
                  )
                }
              })
        }, 
        logOut() {
            axios.post('/api/logout')
                .then(() => location.href = '/web/index.html')
        }    
    }
})
app.mount('#content')