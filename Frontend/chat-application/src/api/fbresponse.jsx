import axios from "axios";

const fbresponse = () => {
    return axios.create({
        baseURL: 'https://graph.facebook.com/v18.0/me/messages?access_token=EAAGZA4ChIhZAkBOZCZCPWhqgI0q9V4YfSr3GnULffSxHWVXDFVJDmajFqts2FZCQfzrW0XV5TOHZAqMjdE8TrZCo60KJJxnrP27eLJ404DyNLWyYHsYKqN0GvPyDlWfpRktcV7iQZAQblxLsUGZCCQaeik8492OcRpISn8EC00qrk9S39jg0m11v3pJGqjvJIhPDH'
    })
}
export default fbresponse