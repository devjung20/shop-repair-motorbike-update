export const yupSync = (schema) => ({
  async validator({ field }, value) {
    await schema.validateSyncAt(field, { [field]: value });
  },
});
