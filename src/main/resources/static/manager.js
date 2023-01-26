const {createApp} = Vue

const app = createApp({
    data() {
        return {
            arrayClients: [],
            clients: [],
            inputName: '',
            inputLast: '',
            inputEmail: '',
            newCLient: {},
            url: "http://localhost:8080/rest/clients",
            showData: {},
            id: [],
            nameLoan: '',
            maxAmount: undefined,
            arrPayments: []
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(this.url)
            .then(result => { 
                this.showData = result.data
                this.clients = result.data._embedded.clients
            })
            .catch(error => console.error(error))
        },
        addClient() {
            if(this.inputName !== "" && this.inputLast !== "" && this.inputEmail !== "" ) {
                this.newCLient = {
                    firstName: this.inputName,
                    lastName: this.inputLast,
                    email: this.inputEmail,
                }
                this.postClient(this.newCLient)
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Oopss..',
                    text: 'Te faltan campos por llenar'
                })
            }
        },
        deleteClient(id) {
            let clientId = id._links.self.href
            Swal.fire({
                title: 'Estas seguro de eliminar el cliente?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(result => {
                if (result.isConfirmed) {
                    axios.delete(clientId)
                    .then(() => this.loadData())
                    Swal.fire(
                        'Eliminado!',
                        'El cliente ha sido eliminado.',
                        'success'
                    )
                }
              })
        },
        postClient(client) {
            axios.post(this.url, client)
            .then( () => this.loadData())
        },
        newLoan() {
            if(this.maxAmount == undefined || this.nameLoan == '' || this.arrPayments.length == 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                  })
            } else {
                const paymentsInt = this.arrPayments.map(prop => parseInt(prop))
                axios.post('/api/loans/admin', `name=${this.nameLoan}&maxAmount=${this.maxAmount}&payments=${paymentsInt}`)
                    .then(() => {
                        Swal.fire(
                            'Loan Created!',
                            'Loan available for everyone!',
                            'success'
                          )
                    })
                    .catch(error => console.error(error))
            }
        } 
    }
})


app.mount("#app")