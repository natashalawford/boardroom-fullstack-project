"use client";

import { z } from "zod"

const updateNameFormSchema = z.object({
    name: z.string().nonempty({
        message: "The name must not be blank"
    })
})

const updatePasswordFormSchema = z.object({
  oldPassword: z.string().nonempty({
    message: "The old password must not be blank"
  }),
  newPassword: z.string().nonempty({
    message: "The new password must not be blank"
  })
})

export { updateNameFormSchema, updatePasswordFormSchema }

