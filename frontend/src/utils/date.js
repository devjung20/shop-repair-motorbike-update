export const addMonth = (date = new Date(), num = 1) => {
  const addedDate = new Date(date);
  addedDate.setMonth(addedDate.getMonth() + num);
  return addedDate;
};
