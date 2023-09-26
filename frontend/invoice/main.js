"use strict";

const BASE_URL = "http://localhost:8080";
const TOKEN_KEY = "jwt_storage_key";
const invoiceId = new URLSearchParams(window.location.search).get("invoiceId");

const dateRenderer = (date = new Date()) => {
  date = new Date(date);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());
  return `${day}/${month}/${year}`;
};

const moneyRenderer = (money = 0) => {
  return money.toLocaleString() + " đ";
};

const serviceLineRenderer = (line) => {
  return `
  <tr>
    <td class="col-6">${line.name}</td>
    <td class="col-2 text-center">${moneyRenderer(line.price)}</td>
    <td class="col-1 text-center">${line.quantity}</td>
    <td class="col-2 text-end">${moneyRenderer(line.price * line.quantity)}</td>
  </tr>
  `;
};

const productLineRenderer = (line) => {
  return `
  <tr>
    <td class="col-4">${line.name}</td>
    <td class="col-2 text-center">${line.unit}</td>
    <td class="col-2 text-center">${moneyRenderer(line.price)}</td>
    <td class="col-1 text-center">${line.quantity}</td>
    <td class="col-2 text-end">${moneyRenderer(line.price * line.quantity)}</td>
  </tr>
  `;
};

const infoDateCode = document.getElementById("info-date-code");
const infoStore = document.getElementById("info-store");
const infoCustomer = document.getElementById("info-customer");
const infoMotorbike = document.getElementById("info-motorbike");
const infoEmployee = document.getElementById("info-employee");
const infoNote = document.getElementById("info-note");
const infoTotalPrice = document.getElementById("info-total-price");
const tableService = document.getElementById("table-service");
const tableProduct = document.getElementById("table-product");
let totalPriceService = 0;
let totalPriceProduct = 0;

const renderDateCode = (invoice) => {
  if (!invoice) return;
  infoDateCode.innerHTML = `
  <div class="col-sm-6"><strong>Ngày:</strong> ${dateRenderer(invoice.modifiedDate)}</div>
  <div class="col-sm-6 text-sm-end"><strong>Mã hóa đơn:</strong> ${invoice.code}</div>
  `;
};

const renderCustomer = (invoice) => {
  if (!invoice) return;
  if (!invoice.infoCustomer) return;

  const customer = invoice.infoCustomer;

  infoCustomer.innerHTML = `
  ${customer.name}<br />
  `;

  if (customer.phone) {
    infoCustomer.innerHTML += `${customer.phone}<br />`;
  }
  if (customer.email) {
    infoCustomer.innerHTML += `${customer.email}<br />`;
  }
  if (customer.address) {
    infoCustomer.innerHTML += `${customer.address}<br />`;
  }
};

const renderStore = (invoice) => {
  if (!invoice) return;
  if (!invoice.storeResponse) return;

  const store = invoice.storeResponse;

  infoStore.innerHTML = "";

  if (store.name) {
    infoStore.innerHTML += `${store.name}<br />`;
  }
  if (store.phone) {
    infoStore.innerHTML += `${store.phone}<br />`;
  }
  if (store.email) {
    infoStore.innerHTML += `${store.email}<br />`;
  }
  if (store.address) {
    infoStore.innerHTML += `${store.address}<br />`;
  }
};

const renderMotorbike = (invoice) => {
  if (!invoice) return;
  if (!invoice.infoCustomer) return;
  infoMotorbike.innerHTML = `
  ${invoice.infoCustomer.motorbikeName}<br />
  ${invoice.infoCustomer.motorbikeCode}<br />
  `;
};

const renderEmployee = (invoice) => {
  if (!invoice) return;
  if (!invoice.infoDispatcher) return;
  if (!invoice.infoRepairer) return;
  infoEmployee.innerHTML = `
  ${invoice.infoDispatcher.name} - Điều phối viên<br />
  ${invoice.infoRepairer.name} - Thợ sửa chữa<br />
  `;
};

const renderNote = (invoice) => {
  if (!invoice) return;
  infoNote.innerHTML = invoice.note;
};

const renderTableService = (invoice) => {
  if (!invoice) return;
  if (!invoice.infoServices) return;

  const services = invoice.infoServices;
  if (services.length == 0) return;
  const totalPrice = services.reduce((sum, s) => (sum += s.price * s.quantity), 0);
  totalPriceService = totalPrice;

  tableService.innerHTML = `
    <thead class="card-header">
      <tr>
        <td class="col-6"><strong>Dịch vụ</strong></td>
        <td class="col-2 text-center"><strong>Đơn giá</strong></td>
        <td class="col-1 text-center"><strong>SL</strong></td>
        <td class="col-2 text-end"><strong>Thành tiền</strong></td>
      </tr>
    </thead>
    <tbody>
      ${services.map((s) => serviceLineRenderer(s)).join("")}
    </tbody>
    <tfoot class="card-footer">
      <tr>
        <td colspan="3" class="text-end"><strong>Tổng tiền:</strong></td>
        <td class="text-end">${moneyRenderer(totalPrice)}</td>
      </tr>
    </tfoot>
  `;
};

const renderTableProduct = (invoice) => {
  if (!invoice) return;
  if (!invoice.infoProducts) return;

  const products = invoice.infoProducts;
  if (products.length == 0) return;
  const totalPrice = products.reduce((sum, s) => (sum += s.price * s.quantity), 0);
  totalPriceProduct = totalPrice;

  tableProduct.innerHTML = `
    <thead class="card-header">
      <tr>
        <td class="col-4"><strong>Linh kiện</strong></td>
        <td class="col-2 text-center"><strong>Đơn vị</strong></td>
        <td class="col-2 text-center"><strong>Đơn giá</strong></td>
        <td class="col-1 text-center"><strong>SL</strong></td>
        <td class="col-2 text-end"><strong>Thành tiền</strong></td>
      </tr>
    </thead>
    <tbody>
      ${products.map((s) => productLineRenderer(s)).join("")}
    </tbody>
    <tfoot class="card-footer">
      <tr>
        <td colspan="4" class="text-end"><strong>Tổng tiền:</strong></td>
        <td class="text-end">${moneyRenderer(totalPrice)}</td>
      </tr>
    </tfoot>
  `;
};

const renderTotalPrice = () => {
  infoTotalPrice.innerHTML = `
  <b>Tổng thanh toán:</b> ${moneyRenderer(totalPriceService + totalPriceProduct)}
  `;
};

const renderInvoice = (invoice) => {
  renderDateCode(invoice);
  renderStore(invoice);
  renderCustomer(invoice);
  renderStore(invoice);
  renderMotorbike(invoice);
  renderEmployee(invoice);
  renderNote(invoice);
  renderTableService(invoice);
  renderTableProduct(invoice);
  renderTotalPrice();
};

// Fetch current invoice
fetch(`${BASE_URL}/api/orders/${invoiceId}`, {
  headers: {
    Authorization: "Bearer " + localStorage.getItem(TOKEN_KEY),
  },
})
  .then((response) => {
    if (!response.ok) {
      throw new Error(`Đường truyền mạng có vấn đề - ${response.status}`);
    }
    return response.json();
  })
  .then((data) => {
    const invoice = data.data;
    renderInvoice(invoice);
  })
  .catch((error) => {
    alert("Có lỗi xảy ra:", error);
    console.error("Có lỗi xảy ra:", error);
  });
