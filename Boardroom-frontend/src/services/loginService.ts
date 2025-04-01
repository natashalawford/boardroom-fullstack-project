import { User } from '@/services/AccountDetailsService'
export interface PersonCreationDto {
  name: string
  email: string
  password: string
  owner: boolean
}

export interface PersonLoginDto {
  email: string
  password: string
}

export interface PersonResponseDto {
  id: number
  name: string
  email: string
  owner: boolean
}

const BASE_URL = 'http://localhost:8080/people'

async function fetchJson<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, options)
  if (!response.ok) {
    let errorMsg = `Error ${response.status}: ${response.statusText}`
    try {
      const errorData = await response.json()
      if (errorData.message) {
        errorMsg = errorData.message
      }
    } catch (err) {
    }
    throw new Error(errorMsg)
  }
  return (await response.json()) as T
}

/** Log in an existing user */
export async function loginUser(
  email: string,
  password: string
): Promise<PersonResponseDto> {
  const loginDto: PersonLoginDto = { email, password }
  return await fetchJson<PersonResponseDto>(`${BASE_URL}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(loginDto),
  })
}

/** Create a new user account */
export async function createUser(
  newPerson: PersonCreationDto
): Promise<PersonResponseDto> {
  return await fetchJson<PersonResponseDto>(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(newPerson),
  })
}

/** Fetch a user by ID */
export async function fetchUser(id: number): Promise<PersonResponseDto> {
  return await fetchJson<PersonResponseDto>(`${BASE_URL}/${id}`)
}

/** Update an existing user's info (except password) */
export async function updateUser(
  id: number,
  partialUpdate: { name: string; email: string; owner: boolean }
): Promise<PersonResponseDto> {
  return await fetchJson<PersonResponseDto>(`${BASE_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(partialUpdate),
  })
}

/** Change a user's password */
export async function changeUserPassword(
  id: number,
  oldPassword: string,
  newPassword: string
): Promise<void> {
  await fetchJson<void>(`${BASE_URL}/${id}/password`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ oldPassword, newPassword }),
  })
}

/** Delete a user by ID */
export async function deleteUser(id: number): Promise<void> {
  await fetchJson<void>(`${BASE_URL}/${id}`, {
    method: 'DELETE',
  })
}

/**
 * Log out by clearing the user in front end with Auth
 */
export function logout(setUserData: (user: User | null) => void) {
  setUserData(null)
}
