package spec.simple

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Spec
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

data class SumRq(val arg : Int)
data class SumRs(val arg : Int)

data class MulRs(val arg : Int)

class ImplicitSumTest {

    companion object : Spec {
        override fun spec(): Specification {
            return specification {
                action { context ->
                    val p: Int = context[SumRq::class]
                    context[SumRs::class] = p + p
                }
            }
        }
    }

    @Test
    fun sumTest() {
        val context = spec().invoke(Context().with(SumRq::class to 5))
        assertEquals(10, context[SumRs::class])
    }


}

class ImplicitMulTest : Spec {

    override fun spec(): Specification {
        return specification {
            action { context ->
                val p: Int = context[SumRs::class]
                context[MulRs::class] = p * p
            }
        }
    }

    @Test
    fun mulTest() {
        val invoke = spec().invoke(Context().with(SumRs::class to 5))
        assertEquals(25, invoke[MulRs::class])
    }

}

class ImplicitSpecInclude {

    @Test
    fun `should sum then multiply`() {
        val context = specification {
            include(ImplicitSumTest.spec())
            include(ImplicitMulTest().spec())
        }.invoke(Context().with(SumRq::class to 5))
        assertEquals(100, context[MulRs::class])
    }

}