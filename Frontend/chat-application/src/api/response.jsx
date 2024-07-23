import axios from "axios";

const response = () => {
    return axios.create({
        baseURL:'http://localhost:9000/'
        //baseURL: 'https://graph.facebook.com/v18.0/me/messages?access_token=EAAGZA4ChIhZAkBO8MDbLgJ3R0KaODhU5me2U8JthusKEcaSLbYqlEko1e9T6xguxMZBWMVKS4y5guHMlLJN0MJlmIQrdG7CK3L2ZCH6X0q1kstDD8DUBmcktc41wQtGHwJkXgZCAZCwodDfqoCtAHzt7zB9HDpe60CZAkCbCfNNl5EUTyvGuq5FpihahChgQTIZBZBZBO9rY0nibZBGH8FJ2gKbWSMeiAZDZD'
    })
}
export default response